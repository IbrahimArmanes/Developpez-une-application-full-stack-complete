import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { finalize } from 'rxjs/operators';

import { PostService } from '../../services/post.service';
import { CommentService } from '../../services/comment.service';
import { PostDto } from '../../models/post.models';
import { CommentDto } from '../../models/comment.models';

@Component({
  selector: 'app-article-detail',
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss']
})
export class ArticleDetailComponent implements OnInit {
  post: PostDto | null = null;
  isLoading = false;
  error: string | null = null;
  
  // New properties for comment functionality
  commentForm: FormGroup;
  isSubmittingComment = false;

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private location: Location,
    private fb: FormBuilder,
    private commentService: CommentService,
    private snackBar: MatSnackBar
  ) {
    // Initialize the form in constructor
    this.commentForm = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(1)]]
    });
  }

  ngOnInit(): void {
    this.isLoading = true;
    this.error = null;
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      const articleId = +idParam; // Convertit la string en nombre
      if (!isNaN(articleId)) {
        this.postService.getPostById(articleId).subscribe({
          next: (data) => {
            this.post = data;
            // Ensure commentaires is initialized as an array if it's null or undefined
            if (!this.post.commentaires) {
              this.post.commentaires = [];
            }
            this.isLoading = false;
          },
          error: (err) => {
            this.error = err.message || 'Impossible de charger l\'article.';
            this.isLoading = false;
            console.error(err);
          }
        });
      } else {
        this.error = 'ID d\'article invalide.';
        this.isLoading = false;
      }
    } else {
      this.error = 'ID d\'article manquant.';
      this.isLoading = false;
    }
  }

  goBack(): void {
    this.location.back();
  }

  /**
   * Submit a new comment for the current article
   */
  onSubmitComment(): void {
    // Check if form is valid and post is loaded
    if (this.commentForm.invalid || !this.post) {
      return;
    }

    this.isSubmittingComment = true;
    const content = this.commentForm.value.content;
    const postId = this.post!.id;

    this.commentService.addComment(postId, content)
      .pipe(
        finalize(() => {
          this.isSubmittingComment = false;
        })
      )
      .subscribe({
        next: (newCommentDto: CommentDto) => {
          // Add the new comment to the post's comments array
          if (this.post && this.post.commentaires) {
            // If commentaires is a Set, convert to array first
            if (!(this.post.commentaires instanceof Array)) {
              this.post.commentaires = Array.from(this.post.commentaires);
            }
            
            // Add the new comment
            this.post.commentaires.push(newCommentDto);
          }
          
          // Reset the form
          this.commentForm.reset();
          
          // Show success message
          this.snackBar.open('Commentaire ajouté avec succès', 'Fermer', {
            duration: 3000,
            horizontalPosition: 'center',
            verticalPosition: 'bottom'
          });
        },
        error: (err) => {
          // Show error message
          this.snackBar.open(
            'Erreur lors de l\'ajout du commentaire: ' + (err.message || 'Veuillez réessayer'),
            'Fermer',
            {
              duration: 5000,
              horizontalPosition: 'center',
              verticalPosition: 'bottom'
            }
          );
          console.error('Error adding comment:', err);
        }
      });
  }
}