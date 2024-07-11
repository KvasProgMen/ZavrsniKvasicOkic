# Enable verbose output for debugging
-verbose

# Suppress warnings for specific classes
-dontwarn com.badlogic.gdx.backends.android.AndroidFragmentApplication

# Required if using Gdx-Controllers extension


# Required if using Box2D extension (Comment out if not using Box2D)
#-keepclassmembers class com.badlogic.gdx.physics.box2d.World {
#   boolean contactFilter(long, long);
#   void    beginContact(long);
#   void    endContact(long);
#   void    preSolve(long, long);
#   void    postSolve(long, long);
#   boolean reportFixture(long);
#   float   reportRayFixture(long, float, float, float, float, float);
#}

# Keep all classes in your package
-keep class com.KvasicOkicIgra.PticaSkakalica.** { *; }

# General keep rules for main application classes
-keep class * extends android.app.Application { *; }
-keep class * extends android.app.Activity { *; }
-keep class * extends android.app.Service { *; }
-keep class * extends android.content.BroadcastReceiver { *; }
-keep class * extends android.content.ContentProvider { *; }
