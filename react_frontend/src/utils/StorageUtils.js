import Utils from "./Utils";

class StorageUtils{

    static setPrivateKey = (privateKey) => (localStorage.setItem("privateKey", privateKey))

    static getPrivateKey = () => (localStorage.getItem("privateKey"));

    static isKeysStored = () => (Utils.validateData(localStorage.getItem("privateKey"), localStorage.getItem("publicKey")))

    static setPublicKey = (publicKey) => (localStorage.setItem("publicKey", publicKey))

    static getPublicKey = () => (localStorage.getItem("publicKey"));

    static resetStorage = () => (localStorage.clear());

}

export default StorageUtils;