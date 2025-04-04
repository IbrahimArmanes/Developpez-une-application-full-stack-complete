import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { SubjectDto } from '../models/subject.models';
import { SubjectSimpleDto } from '../models/subject.models';

@Injectable({
  providedIn: 'root'
})
export class SubjectService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  /**
   * Get all subjects
   * @returns Observable of all subjects in simple form
   */
  getAllSubjects(): Observable<SubjectSimpleDto[]> {
    return this.http.get<SubjectSimpleDto[]>(`${this.apiUrl}/subjects`)
      .pipe(
        catchError(error => {
          console.error('Error fetching subjects', error);
          return throwError(() => new Error('Failed to fetch subjects. Please try again later.'));
        })
      );
  }

  /**
   * Get a subject by its ID
   * @param id The subject ID
   * @returns Observable of the subject
   */
  getSubjectById(id: number): Observable<SubjectDto> {
    return this.http.get<SubjectDto>(`${this.apiUrl}/subjects/${id}`)
      .pipe(
        catchError(error => {
          console.error(`Error fetching subject with ID ${id}`, error);
          return throwError(() => new Error('Failed to fetch subject details. Please try again later.'));
        })
      );
  }

  /**
   * Create a new subject
   * @param subject The subject to create
   * @returns Observable of the created subject
   */
  createSubject(subject: SubjectDto): Observable<SubjectDto> {
    return this.http.post<SubjectDto>(`${this.apiUrl}/subjects`, subject)
      .pipe(
        catchError(error => {
          console.error('Error creating subject', error);
          return throwError(() => new Error('Failed to create subject. Please try again later.'));
        })
      );
  }

  /**
   * Update a subject
   * @param subject The subject to update
   * @returns Observable of the updated subject
   */
  updateSubject(subject: SubjectDto): Observable<SubjectDto> {
    return this.http.put<SubjectDto>(`${this.apiUrl}/subjects/${subject.id}`, subject)
      .pipe(
        catchError(error => {
          console.error(`Error updating subject with ID ${subject.id}`, error);
          return throwError(() => new Error('Failed to update subject. Please try again later.'));
        })
      );
  }

  /**
   * Delete a subject
   * @param id The subject ID to delete
   * @returns Observable of void
   */
  deleteSubject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/subjects/${id}`)
      .pipe(
        catchError(error => {
          console.error(`Error deleting subject with ID ${id}`, error);
          return throwError(() => new Error('Failed to delete subject. Please try again later.'));
        })
      );
  }

  /**
   * Subscribe the authenticated user to a subject
   * @param subjectId The subject ID to subscribe to
   * @returns Observable of void
   */
  subscribeToSubject(subjectId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/subjects/${subjectId}/subscribe`, {})
      .pipe(
        catchError(error => {
          console.error(`Error subscribing to subject with ID ${subjectId}`, error);
          return throwError(() => new Error('Failed to subscribe to subject. Please try again later.'));
        })
      );
  }

  /**
   * Unsubscribe the authenticated user from a subject
   * @param subjectId The subject ID to unsubscribe from
   * @returns Observable of void
   */
  unsubscribeFromSubject(subjectId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/subjects/${subjectId}/subscribe`)
      .pipe(
        catchError(error => {
          console.error(`Error unsubscribing from subject with ID ${subjectId}`, error);
          return throwError(() => new Error('Failed to unsubscribe from subject. Please try again later.'));
        })
      );
  }
}
