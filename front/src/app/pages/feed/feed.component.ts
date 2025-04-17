import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSelectChange } from '@angular/material/select'; // Import pour le type d'événement
import { PostService } from '../../services/post.service';
import { PostSimpleDto } from '../../models/post.models';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent implements OnInit {
  posts: PostSimpleDto[] = [];
  isLoading = false;
  error: string | null = null;
  currentSort: 'asc' | 'desc' = 'desc'; // Défaut à 'desc' comme dans le service

  constructor(
    private postService: PostService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadFeed();
  }

  loadFeed(): void {
    this.isLoading = true;
    this.error = null;
    this.postService.getFeed(this.currentSort).subscribe({
      next: (data) => {
        this.posts = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = err.message || 'Impossible de charger le flux.';
        this.isLoading = false;
        console.error(err);
      }
      // complete: () => { this.isLoading = false; } // Alternative à mettre isLoading=false dans next/error
    });
  }

  // Gère le changement de sélection dans le MatSelect
  changeSort(event: MatSelectChange): void {
    const newSort = event.value;
    if (newSort === 'asc' || newSort === 'desc') {
      this.currentSort = newSort;
      this.loadFeed();
    }
  }

  navigateToDetail(postId: number): void {
    this.router.navigate(['/article', postId]);
  }

  navigateToCreateArticle(): void {
    this.router.navigate(['/create-post']);
  }
}