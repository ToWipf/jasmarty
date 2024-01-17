import Eee from "./components/Eee";
import Fff from "./components/Fff";

const App = () => {
  return (
    <div className="bg-primary text-white w-full h-full">
      <Eee />
      <div className="container px-5 md:px-10 mx-auto">
        <Fff />
      </div>
    </div>
  );
};

export default App;
