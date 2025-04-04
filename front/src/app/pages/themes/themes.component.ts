import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { forkJoin, catchError, of } from 'rxjs';
import { SubjectSimpleDto } from '../../models/subject.models';
import { SubjectService } from '../../services/subject.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss']
})
export class ThemesPageComponent implements OnInit {
  subjects: SubjectSimpleDto[] = [];
  subscribedSubjectIds: Set<number> = new Set<number>();
  isLoading = false;
  error: string | null = null;

  constructor(
    private subjectService: SubjectService,
    private userService: UserService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    this.error = null;

    // Get current user profile and all subjects in parallel
    forkJoin({
      profile: this.userService.getProfile().pipe(
        catchError(error => {
          console.error('Error fetching user profile', error);
          this.error = 'Failed to load user profile. Please try again later.';
          this.snackBar.open(this.error, 'Close', { duration: 5000 });
          return of(null);
        })
      ),
      subjects: this.subjectService.getAllSubjects().pipe(
        catchError(error => {
          console.error('Error fetching subjects', error);
          this.error = 'Failed to load subjects. Please try again later.';
          this.snackBar.open(this.error, 'Close', { duration: 5000 });
          return of([]);
        })
      )
    }).subscribe({
      next: (result) => {
        // Store subjects
        this.subjects = result.subjects;

        // Store subscribed subject IDs
        if (result.profile && result.profile.abonnements) {
          this.subscribedSubjectIds = new Set(
            result.profile.abonnements.map(subject => subject.id)
          );
        }
      },
      error: (error) => {
        console.error('Error loading data', error);
        this.error = 'An unexpected error occurred. Please try again later.';
        this.snackBar.open(this.error, 'Close', { duration: 5000 });
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  subscribe(subjectId: number): void {
    this.subjectService.subscribeToSubject(subjectId).subscribe({
      next: () => {
        this.subscribedSubjectIds.add(subjectId);
        this.snackBar.open('Successfully subscribed to the subject', 'Close', { duration: 3000 });
      },
      error: (error) => {
        console.error(`Error subscribing to subject ${subjectId}`, error);
        this.snackBar.open('Failed to subscribe to the subject. Please try again.', 'Close', { duration: 5000 });
      }
    });
  }

  unsubscribe(subjectId: number): void {
    this.subjectService.unsubscribeFromSubject(subjectId).subscribe({
      next: () => {
        this.subscribedSubjectIds.delete(subjectId);
        this.snackBar.open('Successfully unsubscribed from the subject', 'Close', { duration: 3000 });
      },
      error: (error) => {
        console.error(`Error unsubscribing from subject ${subjectId}`, error);
        this.snackBar.open('Failed to unsubscribe from the subject. Please try again.', 'Close', { duration: 5000 });
      }
    });
  }

  isSubscribed(subjectId: number): boolean {
    return this.subscribedSubjectIds.has(subjectId);
  }

  // Refresh data after subscription changes
  refreshData(): void {
    this.loadData();
  }
}
