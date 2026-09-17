"use client";
import { Button } from "@/components/ui/button";
import { User, UserSchema } from "@/objects/User";
import { registerService } from "@/services/auth/register";
import { RootState } from "@/store/store";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";

export default function Register() {
    const [form, setForm] = useState<UserSchema>({
        fullname: null,
        email: null,
        phone: null,
        password: null
    });
    const [message, setMessage] = useState(null);
    
    const router = useRouter();
    const isLoggedIn = useSelector((state: RootState) => state.auth.isLoggedIn );

    useEffect(() => {
        if (isLoggedIn) {
            router.replace("/");
        }
    }, [isLoggedIn, router]);

    async function handleSubmit(){
        
        const result = User.safeParse(form);
        if (result.success) {
            const response = await registerService(form);
            setMessage(response.message);
            router.replace("/login");
        } else {
            result.error.issues.map((errorDetails) => {
                setMessage(errorDetails.message);
            })
        }

    }
    return (
        <>
            <div className="flex justify-center items-center h-[80vh]">
                <div className="w-[400px] py-10 px-12 border-1 border-solid border-amber-100/20 rounded-md">
                    <h2 className="text-center text-zinc-50">
                        SIGN UP
                    </h2>
                    { message  && 
                        (<div className="font-green-700 text-red-700 text-center">{message}</div>)}
                    <form onSubmit={handleSubmit} className={"form"}>
                        <div className="w-[100%]">
                            <input type="text" onChange={(e) => setForm({ ...form, fullname: e.target.value })} placeholder="Full Name" />
                        </div>
                        <div className="w-[100%]">
                            <input type="email" onChange={(e) => setForm({ ...form, email: e.target.value })} placeholder="Email Address" />
                        </div>
                        <div className="w-[100%]">
                            <input type="password" onChange={(e) => setForm({ ...form, password: e.target.value })}placeholder="Password" />
                        </div>
                        <div className="w-[100%]">
                            <input type="text" onChange={(e) => setForm({ ...form, phone: e.target.value })} placeholder="Phone Number" />
                        </div>
                        <div className="w-[100%] button-group text-center mt-2">
                            <Button className={'login-btn w-[60%] text-md py-3 font-thin tracking-widest'} onClick={handleSubmit} size={"lg"}>SIGN UP</Button>
                        </div>
                        <div className="text-center mt-7 pt-5 border-t-1">
                            <Link href={"/login"} >SIGN IN</Link>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
}
