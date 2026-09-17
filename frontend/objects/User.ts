import * as z from "zod"; 

export const User = z.object({
  id: z.string().nullable().optional(),
  fullname: z.string().min(3, "Name should have atleast 3 letters").nullable().optional(),
  email: z.email("Please enter a valid email address").nullable(),
  password: z.string().min(6, "Password should have atleast 6 characters").nullable(),
  phone: z.string().min(10, "Phone number should have 10 characters").nullable().optional(),
  created_at: z.string().nullable().optional()
});

export type UserSchema = z.infer<typeof User>;