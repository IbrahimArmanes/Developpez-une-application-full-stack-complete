import { CommentDto } from './comment.models';
import { SubjectSimpleDto } from './subject.models';
import { UserSimpleDto } from './user.models';

export interface PostSimpleDto {
  id: number;
  titre: string;
  date: string;
}

export interface PostDto {
  id: number;
  titre: string;
  contenu: string;
  date: string;
  auteur: UserSimpleDto;
  theme: SubjectSimpleDto;
  commentaires: CommentDto[];
}
