uniform sampler2D texID;

varying vec3 vPosition;
varying vec3 vNormal;
varying vec3 vColor;
varying vec2 vTex;

void main(void) {

    vec4 texColor = texture2D(texID, vTex);


    vec4 color = vec4(vColor.x, vColor.y, vColor.z, 1);
    gl_FragColor = texColor * 0.8 + color * 0.2;

}
