const connectConfig = {
    // ip: "192.168.0.131",
    ip: "localhost",
    port: "9002"
};

const historyConfig = {
    ip: "localhost",
    port: "9003"
};

const urlConfig = {
    allMovies: "/v1/movie/all",
    oneMovie: "/v1/movie/id",
    arrayMovie: "/v1/movie/ids",
    allType: "/v1/movie/type",
    register: "/v1/user/register",
    login: "/v1/user/login",
    logout: "/v1/user/logout",
    userInfo: "/v1/user/id",
    oneHistory: "/v1/history/item",
    ratingMovie: "/v1/history/add-item",
    allHistoryTime: "/v1/history/page-userId-timestamp",
    allHistoryRating: "/v1/history/page-userId-rating"
};

const pageConfig = {
    pageSize: 18,
    historySize: 12
};

const exp_time = 2 * 24 * 60 * 60 * 1000;