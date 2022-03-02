export default function(){
        let date = new Date();
        let currentYear = date.getFullYear();
        let currentMonth = date.getMonth() + 1;
        let currentDate = date.getDate();
        let currentHour = date.getHours();
        let currentMinutes = date.getMinutes();
        let currentSeconds = date.getSeconds()
        currentMonth = currentMonth < 10 ? '0' + currentMonth : currentMonth
        currentDate = currentDate < 10 ? '0' + currentDate : currentDate
        currentHour = currentHour < 10 ? '0' + currentHour : currentHour
        currentMinutes = currentMinutes < 10 ? '0' + currentMinutes : currentMinutes
        currentSeconds = currentSeconds < 10 ? '0' + currentSeconds : currentSeconds
        return currentYear + '-' + currentMonth + '-' + currentDate + ' ' + currentHour + ':' + currentMinutes + ':' + currentSeconds
}