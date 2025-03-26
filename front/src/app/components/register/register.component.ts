import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service'; // Adjust path if necessary
import { Location } from '@angular/common'; // Import Location

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  isSubmitting = false;
  hidePassword = true;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar,
    private location: Location // Inject Location
  ) {
    // Initialize the form group
    this.registerForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        this.passwordValidator // Use the custom validator
      ]]
    });
  }

  ngOnInit(): void {
    // If user is already logged in, redirect them away from the register page
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/']); // Or navigate to another appropriate route like '/posts'
    }
  }

  // Custom validator for password strength
  passwordValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;

    // Don't validate if there's no value yet
    if (!value) {
      return null;
    }

    // Check for required character types
    const hasUpperCase = /[A-Z]/.test(value);
    const hasLowerCase = /[a-z]/.test(value);
    const hasNumeric = /[0-9]/.test(value);
    // Updated special character regex to match common requirements
    const hasSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/.test(value);

    const passwordValid = hasUpperCase && hasLowerCase && hasNumeric && hasSpecialChar;

    // Return null if valid, otherwise return an object with flags for missing requirements
    return !passwordValid ? {
      passwordStrength: {
        hasUpperCase,
        hasLowerCase,
        hasNumeric,
        hasSpecialChar
      }
    } : null;
  }

  // Error message generator for the email field
  getEmailErrorMessage(): string {
    const emailControl = this.registerForm.get('email');
    if (emailControl?.hasError('required')) {
      return 'Email is required';
    }
    // Check specifically for the 'email' validator error
    return emailControl?.hasError('email') ? 'Please enter a valid email address' : '';
  }

  // Error message generator for the username field
  getUsernameErrorMessage(): string {
    const usernameControl = this.registerForm.get('username');
    if (usernameControl?.hasError('required')) {
      return 'Username is required';
    }
    if (usernameControl?.hasError('minlength')) {
      // Access the requiredLength property from the error object
      const requiredLength = usernameControl.getError('minlength')?.requiredLength;
      return `Username must be at least ${requiredLength} characters`;
    }
    if (usernameControl?.hasError('maxlength')) {
       // Access the requiredLength property from the error object
      const requiredLength = usernameControl.getError('maxlength')?.requiredLength;
      return `Username cannot exceed ${requiredLength} characters`;
    }
    return ''; // No error
  }

  // Error message generator for the password field
  getPasswordErrorMessage(): string {
    const passwordControl = this.registerForm.get('password');
    if (passwordControl?.hasError('required')) {
      return 'Password is required';
    }
    if (passwordControl?.hasError('minlength')) {
      const requiredLength = passwordControl.getError('minlength')?.requiredLength;
      return `Password must be at least ${requiredLength} characters`;
    }
    // Check for the custom password strength validator error
    if (passwordControl?.hasError('passwordStrength')) {
      const errors = passwordControl.getError('passwordStrength');
      let message = 'Password must contain at least one: ';
      const missing: string[] = [];
      if (!errors.hasUpperCase) missing.push('uppercase letter');
      if (!errors.hasLowerCase) missing.push('lowercase letter');
      if (!errors.hasNumeric) missing.push('number');
      if (!errors.hasSpecialChar) missing.push('special character');
      return message + missing.join(', ');
    }
    return ''; // No error
  }

  // Handle form submission
  onSubmit(): void {
    // Stop if the form is invalid
    if (this.registerForm.invalid) {
      // Optionally mark all fields as touched to show errors
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true; // Set submitting flag

    // Call the AuthService register method
    this.authService.register(this.registerForm.value).subscribe({
      next: (response) => {
        this.isSubmitting = false; // Reset submitting flag
        this.snackBar.open('Registration successful! Please login.', 'Close', {
          duration: 5000, // Duration in milliseconds
          panelClass: ['success-snackbar'] // Optional: Add custom CSS class
        });
        // Navigate to the login page after successful registration
        this.router.navigate(['/login']);
      },
      error: (error) => {
        this.isSubmitting = false; // Reset submitting flag
        // Display error message from the backend or a generic one
        this.snackBar.open(error.message || 'Registration failed. Please try again.', 'Close', {
          duration: 7000, // Longer duration for errors
          panelClass: ['error-snackbar'] // Optional: Add custom CSS class
        });
      }
    });
  }

  // Method for the back button to navigate back in browser history
  goBack(): void {
    this.location.back();
  }
}