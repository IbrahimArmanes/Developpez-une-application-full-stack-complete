import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ThemesPageComponent } from './pages/themes/themes.component';
import { AuthGuard } from './guards/auth.guard';
import { FeedComponent } from './pages/feed/feed.component';
import { ArticleDetailComponent } from './pages/article-detail/article-detail.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    component: HomeComponent,
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'themes',
    component: ThemesPageComponent,
    canActivate: [AuthGuard] 
  },
  {
    path: 'feed',
    component: FeedComponent,
    canActivate: [AuthGuard] 
  },
  {
    path: 'article/:id', 
    component: ArticleDetailComponent,
    canActivate: [AuthGuard] 
  },
  {
    path: '**',
    redirectTo: '/home'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
