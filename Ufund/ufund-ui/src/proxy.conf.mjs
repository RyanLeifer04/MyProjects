export default [
  {
    context: [
      "/users",
      "/cupboard",
      "/basket",
      "/notifications",
    ],
    target: 'http://localhost:8080',
    secure: false,
    changeorigin: true,
  }
];
