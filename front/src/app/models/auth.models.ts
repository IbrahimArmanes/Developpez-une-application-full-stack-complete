export interface RegisterRequest {
    email: string;
    username: string;
    password: string;
  }
  
  export interface LoginRequest {
    username: string;
    password: string;
  }
  
  export interface LoginResponse {
    token: string;
    id: number;
    username: string;
    email: string;
  }
  
  export interface UserResponse {
    id: number;
    username: string;
    email: string;
  }
  
  export interface User {
    id: number;
    username: string;
    email: string;
  }
  