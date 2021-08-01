import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface INote {
  id?: number;
  content?: string;
  password?: string;
  link?: string | null;
  expirationDate?: string;
  user?: IUser | null;
}

export const defaultValue: Readonly<INote> = {};
