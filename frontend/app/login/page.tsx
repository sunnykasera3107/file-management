"use client";
import { Button } from "@/components/ui/button";
import { UserSchema } from "@/objects/User";
import { loginService } from "@/services/auth/login";
import { login } from "@/store/slices/authSlice";
import { RootState } from "@/store/store";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

export default function Login() {
  const emailRef = useRef<HTMLInputElement>(null);
  const passwordRef = useRef<HTMLInputElement>(null);
  const dispatch = useDispatch();
  const isLoggedIn = useSelector((state: RootState) => state.auth.isLoggedIn );
  const [form, setForm] = useState({
    email: "",
    password: ""
  });

  const router = useRouter();

  useEffect(() => {
    if (isLoggedIn) {
      router.replace("/");
    }

    const timer = setTimeout(() => {
      setForm(prev => ({
        ...prev,
        email: emailRef.current?.value || prev.email,
        password: passwordRef.current?.value || prev.password
      }));
    }, 100);

    return () => clearTimeout(timer);
  }, [isLoggedIn, router]);

  const handleLogin = async () => {
    const user: UserSchema = await loginService(form);
    if (user != null && "email" in user) {
      dispatch(login(user));
      router.replace("/");
    }
  }


  return (
    <div className="bg-black pt-5">
        <div className="flex justify-center items-center h-[80vh]">
            <div className="w-[400px] py-10 px-12 border-1 border-solid border-amber-100/20 rounded-md">
                <h2 className="text-center text-zinc-50">
                    SIGN IN
                </h2>
                <form onSubmit={handleLogin} className={"form"}>
                    <div className="w-[100%]">
                        <input ref={emailRef} type="email" onChange={(e) => setForm({ ...form, email: e.target.value })} placeholder="Email Address" />
                    </div>
                    <div className="w-[100%]">
                        <input ref={passwordRef} type="password" onChange={(e) => setForm({ ...form, password: e.target.value })} placeholder="Password" />
                    </div>
                    <div className="w-[100%] button-group text-center mt-2">
                        <Button className={'login-btn w-[60%] text-md py-3 font-thin tracking-widest'} onClick={handleLogin} size={"lg"}>SIGN IN</Button>
                    </div>
                    <div className="text-center mt-7 pt-5 border-t-1">
                        <Link href={"/register"} >SIGN UP</Link>
                    </div>
                </form>
            </div>
        </div>
    </div>
  );
}
