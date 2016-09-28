
attribute vec3 aPosition;
attribute vec3 aNormal;
attribute vec3 aColor;
attribute vec2 aTex;

varying vec3 vPosition;
varying vec3 vNormal;
varying vec3 vColor;
varying vec2 vTex;

uniform sampler2D texID;

uniform mat4 MVP;

void main() {

    gl_Position = MVP * vec4(aPosition, 1);

    vNormal = aNormal;
    vPosition = aPosition;
    vColor = aColor;
    vTex = aTex;

}
