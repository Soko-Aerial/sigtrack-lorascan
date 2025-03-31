<h1 align="center">SIGTRACK LORASCAN LIBRARY</h1>

Add it in your root settings.gradle at the end of repositories:

	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

Add the dependency

	dependencies {
	        implementation 'com.github.Soko-Aerial:sigtrack-lorascan:1.0.0'
	}