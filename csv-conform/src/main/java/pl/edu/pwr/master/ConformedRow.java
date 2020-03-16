package pl.edu.pwr.master;

import java.util.List;

public class ConformedRow {

    public static final String CLASS_TYPE = "class";
    public static final String METHOD_TYPE = "method";
    public static final String CONSTRUCTOR_TYPE = "constructor";

    private String type;
    private String packageName;
    private String outerClassName;
    private String className;
    private String methodName;
    private List<String> parameters;
    private String repository;
    private String commitHash;
    private String gitSourceFileUrl;

    public ConformedRow() {
    }

    public ConformedRow(String type, String packageName, String outerClassName, String className, String methodName, List<String> parameters, String repository, String commitHash, String gitSourceFileUrl) {
        this.type = type;
        this.packageName = packageName;
        this.outerClassName = outerClassName;
        this.className = className;
        this.methodName = methodName;
        this.parameters = parameters;
        this.repository = repository;
        this.commitHash = commitHash;
        this.gitSourceFileUrl = gitSourceFileUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOuterClassName() {
        return outerClassName;
    }

    public void setOuterClassName(String outerClassName) {
        this.outerClassName = outerClassName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }

    public String getGitSourceFileUrl() {
        return gitSourceFileUrl;
    }

    public void setGitSourceFileUrl(String gitSourceFileUrl) {
        this.gitSourceFileUrl = gitSourceFileUrl;
    }
}
