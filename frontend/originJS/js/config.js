const connectConfig = {
    // ip: "192.168.0.131",
    ip: "localhost",
    port: "8999"
    // port: "9002"
};

const urlConfig = {
    allMovies: "/v1/movie/all",
    oneMovie: "/v1/movie/id",
    arrayMovie: "/v1/movie/ids",
    searchMovie: "/v1/movie/search",
    allType: "/v1/movie/type",
    register: "/v1/user/register",
    login: "/v1/user/login",
    logout: "/v1/user/logout",
    loginCheck: "/v1/user/loginCheck",
    userInfo: "/v1/user/id",
    oneHistory: "/v1/history/item",
    ratingMovie: "/v1/history/add-item",
    allHistoryTime: "/v1/history/page-userId-timestamp",
    allHistoryRating: "/v1/history/page-userId-rating",
    recommend: "/v1/recommend/id"
};

const pageConfig = {
    pageSize: 18,
    historySize: 12
};

const exp_time = 2 * 24 * 60 * 60 * 1000;