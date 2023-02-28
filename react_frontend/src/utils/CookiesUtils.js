import Cookies from "universal-cookie";
import * as api from "./api";

class CookiesUtils {

    static setAuthToken(authToken) {
        const cookies = new Cookies();
        let tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() +1);
        cookies.set("authToken", authToken, {path: "/", expires: tomorrow, secure: true});
    }

    static async checkTokenIsValid() {
        const cookies = new Cookies();

        if (cookies.get("authToken"))
            return await api.validToken(cookies.get("authToken"))
                .then(() => {
                    return true
                })
                .catch(() => {
                    return false
                });
        else return false;
    }

    static getAuthToken(){
        const cookies = new Cookies();
        return cookies.get("authToken");
    }

    static removeAuthToken(){
        const cookies = new Cookies();
        cookies.remove("authToken");
    }
}
export default CookiesUtils;

