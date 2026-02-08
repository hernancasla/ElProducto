export interface AdminUser {
  id: string;
  username: string;
  role: "admin";
}

export interface SessionData {
  user: AdminUser;
  isLoggedIn: boolean;
  loginTime: number;
  ipAddress?: string;
}

export interface LoginCredentials {
  username: string;
  password: string;
}
