// 🎯 Importing useful tools from React
import { useState, type Dispatch, type SetStateAction } from "react";



// 🖼️ Styling for this modal
import classes from "./Model.module.scss";
import { Button } from "../../../../components/button/Button";
import { Input } from "../../../../components/input/Input";

// 📦 Props expected by this component (aka instructions from parent)
interface PostingMadalProps {
  showModal: boolean; // Should we show the modal?
  content?: string;   // Optional: pre-filled content in text area
  picture?: string;   // Optional: pre-filled image URL
  setShowModal: Dispatch<SetStateAction<boolean>>; // Function to close/open modal
  onSubmit: (content: string, picture: string) => Promise<void>; // Callback for form submission
  title: string; // Title at the top of the modal
}

// 🪄 Here's the magic modal component itself
export function Madal({
  setShowModal, // To close modal
  showModal,    // Whether modal should be visible
  onSubmit,     // Function to run when "Post" is clicked
  content,      // Pre-filled text (optional)
  picture,      // Pre-filled image URL (optional)
  title,        // Modal title
}: PostingMadalProps) {
  // 🧠 State to track any error message
  const [error, setError] = useState("");

  // 🌀 Track if the form is currently submitting
  const [isLoading, setIsLoading] = useState(false);

  // 🛑 If showModal is false, don’t render anything
  if (!showModal) return null;

  // ✨ Here's the actual modal
  return (
    <div className={classes.root}>
      <div className={classes.modal}>
        {/* 🧠 Header: title + close button */}
        <div className={classes.header}>
          <h3 className={classes.title}>{title}</h3>
          <button onClick={() => setShowModal(false)}>X</button> {/* ❌ Close modal */}
        </div>

        {/* 📬 The form to post your content */}
        <form
          onSubmit={async (e) => {
            e.preventDefault(); // 🛑 Don’t refresh page
            setIsLoading(true); // ⏳ Show loading

            // ✍️ Get values from form fields
            const content = e.currentTarget.content.value;
            const picture = e.currentTarget.picture.value;

            // 🔍 Validate input
            if (!content) {
              setError(""); // ❌ No error, just skip
              setIsLoading(false);
              return;
            }

            // 🚀 Try to submit
            try {
              await onSubmit(content, picture); // 📤 Send data to server
            } catch (error) {
              // ⚠️ Handle errors nicely
              if (error instanceof Error) {
                setError(error.message);
              } else {
                setError("An error occurred. Please try again later.");
              }
            } finally {
              // ✅ Done loading
              setIsLoading(false);
              setShowModal(false); // 🔐 Close modal
            }
          }}
        >
          {/* 🧾 Body with text area and optional image link */}
          <div className={classes.body}>
            <textarea
              placeholder="What do you want to talk about?" // 💬 Prompt text
              onFocus={() => setError("")} // 🔄 Clear error when typing
              onChange={() => setError("")}
              name="content"
              defaultValue={content} // 📝 If already filled
            />
            <Input
              defaultValue={picture}
              placeholder="Image URL (optional)"
              name="picture"
              style={{
                marginBlock: 0,
              }}
            />
          </div>

          {/* 🔥 Error display (if any) */}
          {error && <div className={classes.error}>{error}</div>}

          {/* 🧃 Submit button */}
          <div className={classes.footer}>
            <Button size="medium" type="submit" disabled={isLoading}>
              Post
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
