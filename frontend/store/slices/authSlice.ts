import { UserSchema } from "@/objects/User";
import { createSlice, current } from "@reduxjs/toolkit"

const initUser: UserSchema = {
    id: null,
    email: null,
    name: null
}

const initialState = {
    user: initUser,
    isLoggedIn: false
}

const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        login(state, action) {
            state.user = {
                ...action.payload
            };
            state.isLoggedIn = true;
        },
        logout(state) {
            sessionStorage.removeItem("user");
            state.user = initUser;
            state.isLoggedIn = false;
        },
        checkAuth(state) {
            const currentUser = sessionStorage.getItem("user")
            if (
                currentUser && 
                currentUser != null && 
                typeof(currentUser) != "undefined"
            ) {
                state.user = {
                    ...JSON.parse(currentUser)
                };
                state.isLoggedIn = true;
            }
        }
    }
})

export const { login, logout, checkAuth } = authSlice.actions;

export default authSlice.reducer;