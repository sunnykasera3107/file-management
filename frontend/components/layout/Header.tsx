"use client";

import Link from "next/link";
import { Button } from "../ui/button";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/store/store";
import { logoutService } from "@/services/auth/logout";
import { logout } from "@/store/slices/authSlice";
import { useRouter } from "next/navigation";

export default function Header() {
    const dispatch = useDispatch();
    const auth = useSelector((state: RootState) => state.auth);
    const router = useRouter();
    
    const handleLogout = () => {
        const response = logoutService();
        dispatch(logout());
        router.replace("/login");
    }
    return (
        <header className="bg-black pt-5">
            <div className="w-full max-w-7xl mx-auto text-center relative">
                <Link href={"/"}>
                    <span className="text-4xl font-thin">File Management System</span>
                </Link>
                { auth.isLoggedIn && 
                    (<span className="absolute right-[25px] top-[20px]">
                        Hi, {auth.user.fullname} <Button variant={"link"} className={"text-sm font-light font-sans tracking-wider"} onClick={handleLogout}>Logout</Button>
                    </span>)
                }
            </div>
        </header>
    )
}
