export interface Operateur {
  id: number;
  username: string;
  email: string;
  role: string;
  status: boolean;
  gender: string | null;
  region: string | null;
  codePostal: number | null;
  pays: string | null;
}
