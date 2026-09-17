"use client";

import { useEffect } from "react";
import { generateCSRF } from "@/services/auth/csrf";
import { getAuthorized } from "@/services/auth/authorize";
import User from "@/objects/User";
import { useDispatch } from "react-redux";
import { login, logout } from "@/store/slices/authSlice";

export default function ApiInitializer() {
    const dispatch = useDispatch();
    let alreadyCalled = false;
    useEffect(() => {
        const authUser = async () => {
            const user: User = (await getAuthorized());
            if (user) {
                dispatch(login(user));
            } else {
                dispatch(logout());
            }
        }
        if (!alreadyCalled)  {
            generateCSRF();
            authUser();
            alreadyCalled = true;
        }        
    }, [alreadyCalled]);

    return null;
}