import {listDoctors} from "./utils/http.ts";

 function App() {

    const d = listDoctors({}).then((x) => x);

  return (
    <>
      <div> Hello World!</div>
    </>
  )
}

export default App
