import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    // Check if user is logged in
    if (this.authService.isLoggedIn()) {
      // If trying to access the home page while logged in, redirect to feed
      if (state.url === '/home') {
        return this.router.parseUrl('/feed');
      }
      // Otherwise, allow access to the protected route
      return true;
    }

    // If not logged in and trying to access a protected route, redirect to login
    return this.router.parseUrl('/login');
  }
}
