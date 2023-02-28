class Utils {
    static validateData = (...data) => {
        let boolean = true;
        data.forEach(element => {
            if (element === undefined || element.length < 6) boolean = false;
        });
        return boolean;
    }

    static isEmptyObject = (object) => {
        return (Object.keys(object).length === 0);
    }

    static isPhoto = (fileType) => {
        const photoFormats = ["image/jpeg", "image/png", "image/apng", "image/avif", "image/svg+xml", "image/webp"];
        return photoFormats.includes(fileType);
    }

    static isVideo = (fileType) => {
        const videoFormats = ["video/mp4", "video/ogg", "video/webm"];
        return videoFormats.includes(fileType)
    }

    static isAudio = (fileType) => {
        const audioFormats = ["audio/wav", "audio/mpeg", "audio/mp4", "audio/aac", "audio/aacp", "audio/flac"]
        return audioFormats.includes(fileType)
    }

    static isNotMediaFile = (fileType, fileId) =>
        (!(this.isVideo(fileType) || this.isPhoto(fileType) || this.isAudio(fileType))) && this.validateData(fileId)
}


export default Utils;