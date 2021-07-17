package flare.passion.util;

import java.util.List;

public class UnitV2RequestBean {
    private String version;//2.0，当前api版本对应协议版本号为2.0，固定值
    private String service_id;//BOT唯一标识，在『我的BOT』的BOT列表中第一列数字即为bot_id
    private String log_id;//开发者需要在客户端生成的唯一id，用来定位请求，响应中会返回该字段。对话中每轮请求都需要一个log_id。
    private Request request;//本轮请求体
    private String session_id;//BOT的session信息

    public UnitV2RequestBean() {
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    /**
     * 本轮请求体对象
     * @author 小帅丶
     *
     */
    public static class Request{
        private String user_id;//与BOT对话的用户id
        private String query;//本轮请求query（用户说的话）
        private QueryInfo query_info;//本轮请求query的附加信息。
        private int bernard_level;//澄清确认的敏感程度。取值范围：0(关闭)、1(中敏感度)、2(高敏感度)。
        private String client_session;//client希望传给BOT的本地信息

        public Request() {
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public QueryInfo getQuery_info() {
            return query_info;
        }

        public void setQuery_info(QueryInfo query_info) {
            this.query_info = query_info;
        }

        public int getBernard_level() {
            return bernard_level;
        }

        public void setBernard_level(int bernard_level) {
            this.bernard_level = bernard_level;
        }

        public String getClient_session() {
            return client_session;
        }

        public void setClient_session(String client_session) {
            this.client_session = client_session;
        }
    }
    /**
     *
     * @author 小帅丶
     *
     */
    public static class QueryInfo{
        private String type;
        private String source;

        public QueryInfo() {

        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
    /**
     *
     * @author 小帅丶
     *
     */
    public static class ClientSession{
        private String client_results;
        private List candidate_options;

        public ClientSession() {
        }

        public String getClient_results() {
            return client_results;
        }

        public void setClient_results(String client_results) {
            this.client_results = client_results;
        }

        public List getCandidate_options() {
            return candidate_options;
        }

        public void setCandidate_options(List candidate_options) {
            this.candidate_options = candidate_options;
        }
    }

    public UnitV2RequestBean(String version, String service_id, String log_id, Request request, String session_id) {
        this.version = version;
        this.service_id = service_id;
        this.log_id = log_id;
        this.request = request;
        this.session_id = session_id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

}
