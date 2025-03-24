import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { RegisterRequest, LoginRequest, LoginResponse, UserResponse, User } from '../models/auth.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl || 'http://localhost:8080/api';
  private tokenExpirationTimer: any;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    this.checkAuthStatus();
  }

  register(user: RegisterRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${this.apiUrl}/auth/register`, user)
      .pipe(
        catchError(this.handleError)
      );
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, credentials)
      .pipe(
        tap(response => {
          this.handleAuthentication(response);
        }),
        catchError(this.handleError)
      );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userData');
    localStorage.removeItem('tokenExpiration');
    this.currentUserSubject.next(null);
    if (this.tokenExpirationTimer) {
      clearTimeout(this.tokenExpirationTimer);
    }
    this.tokenExpirationTimer = null;
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }
    
    const expirationDate = this.getTokenExpirationDate();
    if (!expirationDate) {
      return false;
    }
    
    return expirationDate > new Date();
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getCurrentUser(): User | null {
    const userData = localStorage.getItem('userData');
    if (!userData) {
      return null;
    }
    return JSON.parse(userData);
  }

  private handleAuthentication(response: LoginResponse): void {
    const { token, ...userData } = response;
    localStorage.setItem('token', token);
    localStorage.setItem('userData', JSON.stringify(userData));
    
    // Set token expiration (assuming JWT expires in 1 hour)
    const expirationDate = new Date(new Date().getTime() + 3600 * 1000);
    localStorage.setItem('tokenExpiration', expirationDate.toISOString());
    
    this.currentUserSubject.next(userData);
    this.autoLogout(3600 * 1000);
  }

  private autoLogout(expirationDuration: number): void {
    this.tokenExpirationTimer = setTimeout(() => {
      this.logout();
    }, expirationDuration);
  }

  private getTokenExpirationDate(): Date | null {
    const expirationDate = localStorage.getItem('tokenExpiration');
    if (!expirationDate) {
      return null;
    }
    return new Date(expirationDate);
  }

  private checkAuthStatus(): void {
    const userData = this.getCurrentUser();
    if (!userData) {
      return;
    }
    
    const expirationDate = this.getTokenExpirationDate();
    if (!expirationDate) {
      return;
    }
    
    const expiresIn = expirationDate.getTime() - new Date().getTime();
    if (expiresIn > 0) {
      this.currentUserSubject.next(userData);
      this.autoLogout(expiresIn);
    } else {
      this.logout();
    }
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      if (error.status === 0) {
        errorMessage = 'Could not connect to the server. Please check your internet connection.';
      } else if (error.error && error.error.message) {
        errorMessage = error.error.message;
      } else {
        errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
      }
    }
    
    return throwError(() => new Error(errorMessage));
  }
}
