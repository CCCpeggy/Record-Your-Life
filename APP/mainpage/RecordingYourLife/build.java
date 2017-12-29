public class build extends groovy.lang.Script {
    public static void main(java.lang.String[] args) {
        new build(new groovy.lang.Binding(args)).run();
    }

    public java.lang.Object run() {
// Top-level build file where you can add configuration options common to all sub-projects/modules.

        buildscript(new groovy.lang.Closure<java.lang.Object>(this, this) {
            public void doCall(java.lang.Object it) {
                repositories(new groovy.lang.Closure<org.gradle.api.artifacts.repositories.MavenArtifactRepository>(build.this, build.this) {
                    public org.gradle.api.artifacts.repositories.MavenArtifactRepository doCall(java.lang.Object it) {
                        return jcenter();
                    }

                    public MavenArtifactRepository doCall() {
                        return doCall(null);
                    }

                });
                dependencies(new groovy.lang.Closure<java.lang.Object>(build.this, build.this) {
                    public java.lang.Object doCall(java.lang.Object it) {
                        return classpath(new java.lang.Object[]{"com.android.tools.build:gradle:2.3.2"});

                        // NOTE: Do not place your application dependencies here; they belong
                        // in the individual module build.gradle files
                    }

                    public java.lang.Object doCall() {
                        return doCall(null);
                    }

                });
            }

            public void doCall() {
                doCall(null);
            }

        });

        allprojects(new groovy.lang.Closure<java.lang.Object>(this, this) {
            public void doCall(org.gradle.api.Project it) {
                repositories(new groovy.lang.Closure<org.gradle.api.artifacts.repositories.MavenArtifactRepository>(build.this, build.this) {
                    public org.gradle.api.artifacts.repositories.MavenArtifactRepository doCall(java.lang.Object it) {
                        return jcenter();
                    }

                    public MavenArtifactRepository doCall() {
                        return doCall(null);
                    }

                });
            }

            public void doCall() {
                doCall(null);
            }

        });

        java.util.LinkedHashMap<java.lang.String, java.lang.Object> map = new java.util.LinkedHashMap<java.lang.String, java.lang.Object>(1);
        map.put("type", Delete);
        return task(clean(map, new groovy.lang.Closure<org.gradle.api.tasks.Delete>(this, this) {
            public org.gradle.api.tasks.Delete doCall(java.lang.Object it) {
                return delete(getRootProject().getBuildDir());
            }

            public Delete doCall() {
                return doCall(null);
            }

        }));

    }

    public build(Binding binding) {
        super(binding);
    }

    public build() {
        super();
    }
}
