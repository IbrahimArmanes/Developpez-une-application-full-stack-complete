import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { UserDto } from '../../models/user.models';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: UserDto | null = null;
  isLoading = false;
  isUpdatingProfile = false;
  isUpdatingPassword = false;
  profileError: string | null = null;
  formProfileError: string | null = null;
  formPasswordError: string | null = null;
  
  profileForm: FormGroup;
  passwordForm: FormGroup;
  
  hideCurrentPassword = true;
  hideNewPassword = true;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private fb: FormBuilder,
    private snackBar: MatSnackBar
  ) {
    // Initialize forms
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      email: ['', [Validators.required, Validators.email]]
    });
    
    this.passwordForm = this.fb.group({
      currentPassword: ['', [Validators.required]],
      newPassword: ['', [
        Validators.required, 
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)
      ]]
    });
  }

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    this.isLoading = true;
    this.profileError = null;
    
    this.userService.getProfile().subscribe({
      next: (userData) => {
        this.user = userData;
        // Update form with user data
        this.profileForm.patchValue({
          username: userData.username,
          email: userData.email
        });
        this.isLoading = false;
      },
      error: (error) => {
        this.profileError = error.message || 'Failed to load user profile';
        this.isLoading = false;
        this.snackBar.open(this.profileError ?? 'An error occurred', 'Close', { duration: 5000 });
      }
    });
  }

  onProfileSubmit(): void {
    if (this.profileForm.invalid) {
      return;
    }
    
    this.isUpdatingProfile = true;
    this.formProfileError = null;
    
    this.userService.updateProfile(this.profileForm.value).subscribe({
      next: (updatedUser) => {
        this.user = updatedUser;
        this.isUpdatingProfile = false;
        this.snackBar.open('Profile updated successfully', 'Close', { duration: 3000 });
      },
      error: (error) => {
        this.formProfileError = error.message || 'Failed to update profile';
        this.isUpdatingProfile = false;
        this.snackBar.open(this.formProfileError ?? 'An error occurred', 'Close', { duration: 5000 });
      }
    });
  }

  onPasswordSubmit(): void {
    if (this.passwordForm.invalid) {
      return;
    }
    
    this.isUpdatingPassword = true;
    this.formPasswordError = null;
    
    this.userService.updatePassword(this.passwordForm.value).subscribe({
      next: (response) => {
        this.isUpdatingPassword = false;
        this.passwordForm.reset();
        this.hideCurrentPassword = true;
        this.hideNewPassword = true;
        this.snackBar.open('Password updated successfully', 'Close', { duration: 3000 });
      },
      error: (error) => {
        this.formPasswordError = error.message || 'Failed to update password';
        this.isUpdatingPassword = false;
        this.snackBar.open(this.formPasswordError ?? 'An error occurred', 'Close', { duration: 5000 });
      }
    });
  }

  unsubscribe(subjectId: number): void {
    this.userService.unsubscribeFromSubject(subjectId).subscribe({
      next: () => {
        // Update local user data by filtering out the unsubscribed subject
        if (this.user && this.user.abonnements) {
          this.user.abonnements = this.user.abonnements.filter(sub => sub.id !== subjectId);
        }
        this.snackBar.open('Unsubscribed successfully', 'Close', { duration: 3000 });
      },
      error: (error) => {
        const errorMessage = error.message || 'Failed to unsubscribe';
        this.snackBar.open(errorMessage, 'Close', { duration: 5000 });
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.snackBar.open('Logged out successfully', 'Close', { duration: 3000 });
  }

  getPasswordErrorMessage(): string {
    const control = this.passwordForm.get('newPassword');
    if (!control) return '';
    
    if (control.hasError('required')) {
      return 'Password is required';
    }
    if (control.hasError('minlength')) {
      return 'Password must be at least 8 characters';
    }
    if (control.hasError('pattern')) {
      return 'Password must include uppercase, lowercase, number and special character';
    }
    
    return '';
  }
}
