export interface Acheteur {
  id: number;
  username: string;
  email: string;
  role: string;
  status: boolean;
  gender: string | null; // Nullable fields
  region: string | null; // Nullable fields
  codePostal: number| null; // Nullable fields
  pays: string | null; // Nullable fields
}
