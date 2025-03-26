import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service'; // Adjust path if necessary

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  // Inject AuthService and make it public
  constructor(public authService: AuthService) {}

  ngOnInit(): void {
    // You can keep or remove the start() method/logic if not needed
  }

  // Method to call when the logout button is clicked
  logout(): void {
    this.authService.logout();
    // The service already handles navigation to /login
  }

}