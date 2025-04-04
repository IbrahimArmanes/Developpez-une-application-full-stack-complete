import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

// Routing
import { AppRoutingModule } from './app-routing.module';

// Components
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ThemesPageComponent } from './pages/themes/themes.component';

// Services
import { AuthService } from './services/auth.service';
import { AuthInterceptor } from './interceptors/auth.interceptor';

// Angular Material Modules
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatListModule } from '@angular/material/list';

@NgModule({
  declarations: [
    AppComponent, 
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    ThemesPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    
    // Angular Material
    MatButtonModule,
    MatCardModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatListModule
  ],
  providers: [
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
