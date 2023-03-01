class TimeUtils{

    static getLastTimeActivityAgo = (milliseconds) => {
        const timeAgoInSec = (new Date().getTime() - milliseconds) / 1000;

        if(timeAgoInSec >= 1 && timeAgoInSec <= 60)
            return {message: "Online", online: true};
        else if (timeAgoInSec > 60 && timeAgoInSec <= 120)
            return {message: "last seen less than minute ago", online: true}
        else if(timeAgoInSec > 120 && timeAgoInSec < (60 * 60))
            return {message: "last seen " + Math.round((timeAgoInSec/60)) + " minutes ago", online: false};
        else if(timeAgoInSec >= 60 && timeAgoInSec < (60*120))
            return {message: "last seen one hour ago", online: false};
        else if(timeAgoInSec >= (60*120) && timeAgoInSec < (60 * 60 * 24))
            return {message: "last seen " + Math.round( timeAgoInSec/(60*60)) + " hours ago", online: false};
        else if(timeAgoInSec >= (60*60*24) && timeAgoInSec < (60*60*24*2))
            return {message: "last seen one day ago", online: false};
        else return {message: "last seen " + Math.round(timeAgoInSec/(60*60*24)) + " days ago", online: false};
    }

    static getDate = (milliseconds) => {
        const date = new Date(milliseconds);
        return date.toLocaleDateString('en-US', { month: 'long', day: 'numeric', year: 'numeric' });
    }

    static dateFormatter = (milliseconds) => {
        const date = new Date(milliseconds);
        const value = (new Date().getTime() - milliseconds) / 1000;

        if(value < (60 * 60 * 24)){
            return date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', hour12: true });
        }else if(value < (60 * 60 * 24 * 7)){
            return date.toLocaleDateString('en-US', { weekday: 'long' });
        }else{
            return TimeUtils.getDate(milliseconds)
        }
    }

}
export default TimeUtils;