import { SessionOptions } from "iron-session";
import { SessionData } from "@/types/auth";

export const sessionOptions: SessionOptions = {
  password: process.env.SESSION_SECRET || "complex_password_at_least_32_characters_long_for_security",
  cookieName: "elproducto_admin_session",
  cookieOptions: {
    secure: process.env.NODE_ENV === "production",
    httpOnly: true,
    maxAge: 60 * 60 * 8, // 8 horas
    sameSite: "lax",
  },
};

export const defaultSession: SessionData = {
  user: { id: "", username: "", role: "admin" },
  isLoggedIn: false,
  loginTime: 0,
};
