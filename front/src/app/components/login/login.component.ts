import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service';
import { Location } from '@angular/common'; 

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  isSubmitting = false;
  hidePassword = true;
  returnUrl: string = '/';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private location: Location 
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    if (this.authService.isLoggedIn()) {
      this.router.navigate([this.returnUrl]);
    }
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      // Marquer les champs comme touchés pour afficher les erreurs si nécessaire
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;

    const credentials = {
      // Assurez-vous que les clés correspondent à ce qu'attend votre LoginRequest DTO backend
      username: this.loginForm.value.username, // Ou email: si le backend attend 'email'
      password: this.loginForm.value.password
    };

    this.authService.login(credentials).subscribe({
      next: (response) => { // Supposons que le service login retourne la réponse avec le token
        this.isSubmitting = false;
        // Le stockage du token devrait être géré dans AuthService ou ici si nécessaire
        // Exemple : localStorage.setItem('authToken', response.token);
        this.router.navigate([this.returnUrl]);
      },
      error: (error) => {
        this.isSubmitting = false;
        // Afficher un message plus spécifique basé sur l'erreur si possible
        const errorMessage = error?.error?.message || error?.message || 'Login failed. Please check your credentials.';
        this.snackBar.open(errorMessage, 'Close', {
          duration: 5000,
          panelClass: ['error-snackbar'] // Assurez-vous que cette classe est définie globalement ou ici
        });
      }
    });
  }

  // Méthode pour le bouton retour
  goBack(): void {
    this.location.back();
  }
}