import { aaa } from "../assets";

const Eee = () => {
  return (
    <section className="relative font-poppins py-10 sm:py-16">
      <div>
        Einfach mein Text
        <div className="flex-1">
          <img
            src={aaa}
            alt="blub"
            className="w-full h-full object-contain md:object-fill"
          />
          </div>
      </div>
    </section>
  );
};

export default Eee;
