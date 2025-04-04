import { PostSimpleDto } from './post.models';
import { UserSimpleDto } from './user.models';

export interface CommentDto {
  id: number;
  contenu: string;
  date: string;
  auteur: UserSimpleDto;
  article: PostSimpleDto;
}
