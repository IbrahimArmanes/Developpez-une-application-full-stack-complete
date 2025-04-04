import { PostSimpleDto } from './post.models';
import { UserSimpleDto } from './user.models';

export interface SubjectSimpleDto {
  id: number;
  nom: string;
}

export interface SubjectDto {
  id: number;
  nom: string;
  posts: PostSimpleDto[];
  abonnes: UserSimpleDto[];
}
