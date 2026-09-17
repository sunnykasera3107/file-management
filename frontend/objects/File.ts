export default interface File {
  id?: string | null;
  filename?: string | null;
  filepath?: string | null;
  size?: number | null;
  metadata?: object | null;
  column_analysis?: object | null;
  created_at?: Date | null;
}