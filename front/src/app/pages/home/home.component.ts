import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service'; // Adjust path if necessary
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {

  constructor(
    public authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      // Redirect to feed if logged in
      this.router.navigate(['/feed']);
    }
  }

  // Method to call when the logout button is clicked
  logout(): void {
    this.authService.logout();
    // The service already handles navigation to /login
  }

}