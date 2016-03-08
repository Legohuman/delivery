(function (undefined) {

    if (!Detector.webgl) Detector.addGetWebGLMessage();

    var container;

    var camera, controls, scene, renderer;

    var cross;

    init();
    animate();

    function init() {
        container = document.getElementById('container');

        camera = new THREE.PerspectiveCamera(60, window.innerWidth / window.innerHeight, 1, 1000);
        camera.position.z = 50;

        controls = new THREE.TrackballControls(camera, container);

        controls.rotateSpeed = 1.0;
        controls.zoomSpeed = 1.2;
        controls.panSpeed = 0.8;

        controls.noZoom = false;
        controls.noPan = false;

        controls.staticMoving = true;
        controls.dynamicDampingFactor = 0.3;

        controls.keys = [65, 83, 68];

        controls.addEventListener('change', render);

        // world

        scene = new THREE.Scene();
        scene.fog = new THREE.FogExp2(0xcccccc, 0.002);

        var geometry = new THREE.BoxGeometry(5, 5, 5);
        var material = new THREE.MeshPhongMaterial({color: 0xff3333, shading: THREE.SmoothShading});
        geometry.scale(-1, -1, -1);

        var mesh = new THREE.Mesh(geometry, material);
        scene.add(mesh);

        var geometry2 = new THREE.BoxGeometry(1, 2, 10);
        var material2 = new THREE.MeshPhongMaterial({color: 0x3333ff, shading: THREE.SmoothShading});

        var mesh2 = new THREE.Mesh(geometry2, material2);
        scene.add(mesh2);

        // lights

        light = new THREE.DirectionalLight(0xffffff);
        light.position.set(1, 1, 1);
        scene.add(light);

        light = new THREE.DirectionalLight(0x002288);
        light.position.set(-1, -1, -1);
        scene.add(light);

        light = new THREE.AmbientLight(0x222222);
        scene.add(light);


        // renderer

        renderer = new THREE.WebGLRenderer({antialias: false});
        renderer.setClearColor(scene.fog.color);
        renderer.setPixelRatio(window.devicePixelRatio);
        renderer.setSize(window.innerWidth, window.innerHeight);

        container.appendChild(renderer.domElement);

        //

        window.addEventListener('resize', onWindowResize, false);
        //

        render();

    }

    function onWindowResize() {

        camera.aspect = window.innerWidth / window.innerHeight;
        camera.updateProjectionMatrix();

        renderer.setSize(window.innerWidth, window.innerHeight);

        controls.handleResize();

        render();

    }

    function animate() {

        requestAnimationFrame(animate);
        controls.update();

    }

    function render() {

        renderer.render(scene, camera);

    }

})();