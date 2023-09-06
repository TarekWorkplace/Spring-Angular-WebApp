export interface Fourniseur {
  id: number;
  username: string;
  email: string;
  role: string;
  status: boolean;
  gender: string;
  region: string | null; // Nullable fields
  codePostal: number | null; // Nullable fields
  pays: string | null; // Nullable fields
  productType: string | null; // Nullable fields
}
