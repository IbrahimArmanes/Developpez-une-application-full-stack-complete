import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { PostService } from '../../services/post.service';
import { PostDto } from '../../models/post.models';

@Component({
  selector: 'app-article-detail',
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss']
})
export class ArticleDetailComponent implements OnInit {
  post: PostDto | null = null;
  isLoading = false;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private location: Location
  ) { }

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

}