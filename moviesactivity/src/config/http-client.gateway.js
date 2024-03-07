import axios from 'axios';

const AxiosClient = axios.create({
  baseURL: 'http://localhost:8080/api/',
  timeout: 3000,
});

export default {
    doGet: function (endPoint, config) {
        return AxiosClient.get(endPoint, config);
    },
    doPost: function (endPoint, object, config) {
        return AxiosClient.post(endPoint, object, config || {});
    },
    doPut: function (endPoint, object) {
        return AxiosClient.put(endPoint, object);
    },
    doDelete: function (endPoint) {
        return AxiosClient.delete(endPoint);
    },
};
