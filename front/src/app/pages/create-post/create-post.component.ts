import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Location } from '@angular/common'; // Import Location for back button
import { SubjectService } from '../../services/subject.service';
import { PostService } from '../../services/post.service';
import { SubjectSimpleDto } from '../../models/subject.models'; // Assurez-vous que le modèle existe
import { PostDto } from 'src/app/models/post.models';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss']
})
export class CreatePostComponent implements OnInit {
  postForm: FormGroup;
  subjects: SubjectSimpleDto[] = [];
  isLoadingSubjects = false;
  isSubmitting = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private subjectService: SubjectService,
    private postService: PostService,
    private router: Router,
    private snackBar: MatSnackBar,
    private location: Location // Inject Location
  ) {
    this.postForm = this.fb.group({
      // Initialisation dans le constructeur pour éviter les problèmes potentiels avec la vue
      themeId: [null, [Validators.required]],
      titre: ['', [Validators.required, Validators.minLength(5)]],
      contenu: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  ngOnInit(): void {
    this.loadSubjects();
  }

  loadSubjects(): void {
    this.isLoadingSubjects = true;
    this.error = null; // Réinitialiser l'erreur au chargement
    this.subjectService.getAllSubjects().subscribe({
      next: (data) => {
        this.subjects = data;
        this.isLoadingSubjects = false;
      },
      error: (err) => {
        console.error('Error loading subjects:', err);
        this.error = 'Impossible de charger les thèmes. Veuillez réessayer.';
        this.snackBar.open(this.error, 'Fermer', { duration: 5000 });
        this.isLoadingSubjects = false;
      }
    });
  }

  onSubmit(): void {
    if (this.postForm.invalid) {
      // Marquer tous les champs comme touchés pour afficher les erreurs
      this.postForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.error = null;

    const formData = this.postForm.value;
    
    // Use type assertion to tell TypeScript this is a valid PostDto
    const postData = {
      titre: formData.titre,
      contenu: formData.contenu,
      theme: {
        id: formData.themeId,
        nom: ''
      }
    } as PostDto;  // Type assertion here

    this.postService.createPost(postData).subscribe({
      next: (createdPost) => {
        this.snackBar.open('Article créé avec succès !', 'Fermer', { duration: 3000 });
        // Naviguer vers le détail de l'article créé
        this.router.navigate(['/article', createdPost.id]);
      },
      error: (err) => {
        console.error('Error creating post:', err);
        this.error = err.message || 'Une erreur est survenue lors de la création de l\'article.';
        this.snackBar.open(this.error || 'Une erreur est survenue', 'Fermer', { duration: 5000, panelClass: ['error-snackbar'] });
        this.isSubmitting = false;
      },      complete: () => {
        this.isSubmitting = false;
      }
    });
  }

  // Méthode pour le bouton retour
  goBack(): void {
    this.location.back();
  }
}