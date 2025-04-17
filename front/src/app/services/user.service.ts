import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';
import { UserDto, PasswordUpdateRequest, MessageResponse } from '../models/user.models';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = environment.apiUrl || 'http://localhost:8080/api';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getProfile(): Observable<UserDto> {
    return this.http.get<UserDto>(`${this.apiUrl}/users/me`)
      .pipe(
        catchError(this.handleError)
      );
  }

  updateProfile(userDto: Omit<UserDto, 'id' | 'abonnements'>): Observable<UserDto> {
    return this.http.put<UserDto>(`${this.apiUrl}/users/me`, userDto)
      .pipe(
        catchError(this.handleError)
      );
  }

  updatePassword(request: PasswordUpdateRequest): Observable<MessageResponse> {
    return this.http.put<MessageResponse>(`${this.apiUrl}/users/me/password`, request)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Updated to use the correct endpoint from SubjectController
  unsubscribeFromSubject(subjectId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/subjects/${subjectId}/subscribe`)
      .pipe(
        catchError(this.handleError)
      );
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
