import { SubjectSimpleDto } from './subject.models';

export interface UserSimpleDto {
  id: number;
  username: string;
}

export interface UserDto {
  id: number;
  email: string;
  username: string;
  abonnements: SubjectSimpleDto[];
}

export interface PasswordUpdateRequest {
  currentPassword: string;
  newPassword: string;
}

export interface MessageResponse {
  message: string;
}