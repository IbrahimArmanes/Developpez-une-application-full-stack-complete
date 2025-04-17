import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { PostSimpleDto, PostDto } from '../models/post.models';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  /**
   * Get personalized feed of posts based on user subscriptions
   * @param sort the sort direction ('asc' or 'desc')
   * @returns Observable of PostSimpleDto array
   */
  getFeed(sort: 'asc' | 'desc' = 'desc'): Observable<PostSimpleDto[]> {
    const params = new HttpParams().set('sort', sort);
    
    return this.http.get<PostSimpleDto[]>(`${this.apiUrl}/posts/feed`, { params })
      .pipe(
        catchError(error => {
          console.error('Error fetching feed:', error);
          return throwError(() => new Error('Failed to load feed. Please try again later.'));
        })
      );
  }

  /**
   * Get a post by its ID
   * @param id the post ID
   * @returns Observable of PostDto
   */
  getPostById(id: number): Observable<PostDto> {
    return this.http.get<PostDto>(`${this.apiUrl}/posts/${id}`)
      .pipe(
        catchError(error => {
          console.error(`Error fetching post with ID ${id}:`, error);
          return throwError(() => new Error('Failed to load post details. Please try again later.'));
        })
      );
  }

  /**
   * Get all posts
   * @returns Observable of PostSimpleDto array
   */
  getAllPosts(): Observable<PostSimpleDto[]> {
    return this.http.get<PostSimpleDto[]>(`${this.apiUrl}/posts`)
      .pipe(
        catchError(error => {
          console.error('Error fetching all posts:', error);
          return throwError(() => new Error('Failed to load posts. Please try again later.'));
        })
      );
  }

  /**
   * Get all posts for a specific subject
   * @param subjectId the subject ID
   * @returns Observable of PostSimpleDto array
   */
  getPostsBySubject(subjectId: number): Observable<PostSimpleDto[]> {
    return this.http.get<PostSimpleDto[]>(`${this.apiUrl}/posts/subject/${subjectId}`)
      .pipe(
        catchError(error => {
          console.error(`Error fetching posts for subject ${subjectId}:`, error);
          return throwError(() => new Error('Failed to load subject posts. Please try again later.'));
        })
      );
  }

  /**
   * Create a new post
   * @param post the post to create
   * @returns Observable of created PostDto
   */
  createPost(post: PostDto): Observable<PostDto> {
    return this.http.post<PostDto>(`${this.apiUrl}/posts`, post)
      .pipe(
        catchError(error => {
          console.error('Error creating post:', error);
          return throwError(() => new Error('Failed to create post. Please try again later.'));
        })
      );
  }

  /**
   * Update a post
   * @param post the post to update
   * @returns Observable of updated PostDto
   */
  updatePost(post: PostDto): Observable<PostDto> {
    return this.http.put<PostDto>(`${this.apiUrl}/posts/${post.id}`, post)
      .pipe(
        catchError(error => {
          console.error(`Error updating post with ID ${post.id}:`, error);
          return throwError(() => new Error('Failed to update post. Please try again later.'));
        })
      );
  }

  /**
   * Delete a post
   * @param id the post ID to delete
   * @returns Observable of void
   */
  deletePost(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/posts/${id}`)
      .pipe(
        catchError(error => {
          console.error(`Error deleting post with ID ${id}:`, error);
          return throwError(() => new Error('Failed to delete post. Please try again later.'));
        })
      );
  }
}
