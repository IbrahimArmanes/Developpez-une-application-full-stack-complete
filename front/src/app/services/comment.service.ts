import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { CommentDto } from '../models/comment.models';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  /**
   * Add a new comment to a post
   * @param postId The ID of the post to comment on
   * @param content The content of the comment
   * @returns An Observable of the created CommentDto
   */
  addComment(postId: number, content: string): Observable<CommentDto> {
    // Construct the comment data as expected by the backend
    const commentData = {
      contenu: content,
      article: { id: postId }
    };

    // Send POST request to the comments endpoint
    return this.http.post<CommentDto>(`${this.apiUrl}/comments`, commentData)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Error handler for HTTP requests
   * @param error The HTTP error response
   * @returns An observable that errors with the formatted error message
   */
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred';
    
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
