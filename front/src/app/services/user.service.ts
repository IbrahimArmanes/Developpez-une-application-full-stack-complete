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
    const currentUser = this.authService.getCurrentUser();
    
    if (!currentUser || !currentUser.id) {
      return throwError(() => new Error('User not logged in'));
    }
    
    const userId = currentUser.id;
    
    return this.http.get<UserDto>(`${this.apiUrl}/users/${userId}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  updateProfile(userDto: UserDto): Observable<UserDto> {
    const currentUser = this.authService.getCurrentUser();
    
    if (!currentUser || !currentUser.id) {
      return throwError(() => new Error('User not logged in'));
    }
    
    // Ensure the userDto ID matches the logged-in user's ID
    if (userDto.id !== currentUser.id) {
      return throwError(() => new Error('Cannot update profile for another user'));
    }
    
    return this.http.put<UserDto>(`${this.apiUrl}/users/${userDto.id}`, userDto)
      .pipe(
        catchError(this.handleError)
      );
  }

  updatePassword(request: PasswordUpdateRequest): Observable<MessageResponse> {
    const currentUser = this.authService.getCurrentUser();
    
    if (!currentUser || !currentUser.id) {
      return throwError(() => new Error('User not logged in'));
    }
    
    const userId = currentUser.id;
    
    return this.http.put<MessageResponse>(`${this.apiUrl}/users/${userId}/password`, request)
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
